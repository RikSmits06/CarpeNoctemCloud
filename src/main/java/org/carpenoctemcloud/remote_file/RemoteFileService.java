package org.carpenoctemcloud.remote_file;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.carpenoctemcloud.configuration.ConfigurationConstants;
import org.carpenoctemcloud.indexing.IndexedFile;
import org.carpenoctemcloud.server.Server;
import org.carpenoctemcloud.server.ServerMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Service;

/**
 * Class with methods to access the RemoteFile table.
 */
@Service
public class RemoteFileService {

    final NamedParameterJdbcTemplate template;

    /**
     * Constructs a new RemoteFileService.
     *
     * @param template The template from JDBC to execute queries in.
     */
    public RemoteFileService(NamedParameterJdbcTemplate template) {
        this.template = template;
    }

    /**
     * Deletes old files from the cache.
     *
     * @param cutOff If the file is cached after the cut-off, it isn't deleted.
     *               If it is older, it gets removed.
     * @return The amount of rows deleted.
     */
    public int deleteOldRemoteFiles(Timestamp cutOff) {
        SqlParameterSource source = new MapSqlParameterSource().addValue("cut_off", cutOff);
        return template.update("DELETE FROM remote_file rf WHERE rf.last_indexed < :cut_off;",
                               source);
    }

    /**
     * Adds a file to the cache database.
     *
     * @param indexedFile The file to add to the database.
     */
    public void addRemoteFile(IndexedFile indexedFile) {
        this.addRemoteFiles(new ArrayList<>(List.of(indexedFile)));
    }


    /**
     * Searches for RemoteFiles matching the search parameter.
     * The list is at most equal to the max fetch size set in {@link ConfigurationConstants}.
     *
     * @param search      The search parameter, can be null or empty.
     * @param offset      The offset of the search.
     * @param categoryIDs The list of category ids to search, can be null or empty.
     * @return The list of remote files matching the input.
     */
    public List<RemoteFile> searchRemoteFiles(String search, int offset, List<Integer> categoryIDs) {
        if (search.isBlank()) {
            search = null;
        }

        SqlParameterSource source =
                new MapSqlParameterSource().addValue("offset", offset).addValue("search", search)
                        .addValue("limit", ConfigurationConstants.MAX_FETCH_SIZE)
                        .addValue("categoryIDs", categoryIDs != null && !categoryIDs.isEmpty() ? categoryIDs : null);

        return template.query("""
                                      select *, ts_rank(search_vector, websearch_to_tsquery(:search)) AS rankings
                                      from remote_file
                                      where (case
                                                 when (cast(:categoryIDs as integer[]) is not null)
                                                     then category_id = ANY(:categoryIDs)
                                                 else true
                                          end)
                                        and (case
                                                 when (cast(:search as text) is not null)
                                                     then search_vector @@ websearch_to_tsquery(:search)
                                                 else true end)
                                      order by rankings desc
                                      limit :limit offset :offset;
                                      """, source, new RemoteFileMapper());
    }

    /**
     * Retrieves a RemoteFile based on its ID.
     *
     * @param id The ID of the RemoteFile.
     * @return The RemoteFile or an empty optional if no RemoteFile exists.
     */
    public Optional<RemoteFile> getRemoteFileByID(long id) {
        SqlParameterSource source = new MapSqlParameterSource().addValue("id", id);
        List<RemoteFile> result =
                template.query("SELECT * FROM remote_file WHERE id=:id LIMIT 1;", source,
                               new RemoteFileMapper());
        if (result.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(result.getFirst());
    }

    /**
     * Gets the url to download from.
     *
     * @param fileID The id of the file.
     * @return The url to the external resource.
     */
    public String getDownloadURL(long fileID) {
        MapSqlParameterSource source = new MapSqlParameterSource().addValue("fileID", fileID);
        return template.query("""
                                      select distinct download_prefix || '://' || host || path || rf.name as url
                                      from remote_file rf,
                                           directory dir,
                                           server ser
                                      where rf.id = :fileID
                                        and rf.directory_id = dir.id
                                        and ser.id = dir.server_id;
                                      """, source, rs -> {
            rs.next();
            return rs.getString("url");
        });
    }

    /**
     * Adds a list of indexed files to the database by collecting them in a batch.
     *
     * @param indexedFiles The list of indexed files to save.
     */
    public void addRemoteFiles(List<IndexedFile> indexedFiles) {
        SqlParameterSource[] sources = new SqlParameterSource[indexedFiles.size()];

        for (int i = 0; i < sources.length; i++) {
            IndexedFile file = indexedFiles.get(i);
            SqlParameterSource source =
                    new MapSqlParameterSource().addValue("name", file.filename())
                            .addValue("server_name", file.host()).addValue("path", file.path());
            sources[i] = source;
        }

        template.batchUpdate("""
                                     with dir as (insert into directory (path, server_id)
                                         values (:path, (select id from server where host = :server_name limit 1))
                                         on conflict on constraint unique_server_path
                                             do update set server_id = directory.server_id returning id)
                                     insert
                                     into remote_file(name, directory_id)
                                     select :name, dir.id
                                     from dir
                                     on conflict on constraint unique_file do update set last_indexed = now();
                                     """, sources);
    }

    /**
     * Gets the host of a certain file.
     *
     * @param fileID The id of the file.
     * @return The string of the hostname of where the file is hosted.
     */
    public Server getServerOfFile(long fileID) {
        SqlParameterSource source = new MapSqlParameterSource().addValue("fileID", fileID);

        return template.query("""
                                      select ser.*
                                      from server ser,
                                           directory dir,
                                           remote_file rf
                                      where rf.id = :fileID
                                        and rf.directory_id = dir.id
                                        and dir.server_id = ser.id
                                      limit 1;
                                      """, source, new ServerMapper()).getFirst();
    }
}
