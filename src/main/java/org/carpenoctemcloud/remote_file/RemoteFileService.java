package org.carpenoctemcloud.remote_file;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import org.carpenoctemcloud.configuration.ConfigurationConstants;
import org.carpenoctemcloud.indexing.IndexedFile;
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
        SqlParameterSource source = new MapSqlParameterSource().addValue("url", indexedFile.url())
                .addValue("name", indexedFile.filename());

        template.update("""
                                 INSERT INTO remote_file (name, download_url, last_indexed)
                                                                 VALUES (:name, :url, now())
                                                                 ON CONFLICT (download_url) DO UPDATE
                                                                   SET last_indexed = excluded.last_indexed;
                                """, source);
    }

    /**
     * Searches for RemoteFiles matching the search parameter.
     * The list is at most equal to the max fetch size set in {@link ConfigurationConstants}.
     *
     * @param search     The search parameter, can be null or empty.
     * @param offset     The offset of the search.
     * @param categoryID The id of the category to search can be null.
     * @return The list of remote files matching the input.
     */
    public List<RemoteFile> searchRemoteFiles(String search, int offset, Integer categoryID) {
        if (search.isBlank()) {
            search = null;
        }

        SqlParameterSource source =
                new MapSqlParameterSource().addValue("offset", offset).addValue("search", search)
                        .addValue("limit", ConfigurationConstants.MAX_FETCH_SIZE)
                        .addValue("cid", categoryID);

        return template.query("""
                                      select *, ts_rank(search_vector, websearch_to_tsquery(:search)) AS rankings
                                      from remote_file
                                      where (case
                                                 when (cast(:cid as integer) is not null)
                                                     then category_id = :cid
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
    public Optional<RemoteFile> getRemoteFileByID(int id) {
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
     * Gets the total number of RemoteFile entries in the database.
     *
     * @return The total number of files in the database.
     */
    public int getTotalFiles(Integer categoryID) {
        SqlParameterSource source = new MapSqlParameterSource().addValue("cid", categoryID);
        return template.query("""
                select count(*) from remote_file where (case when (cast(:cid as integer) is not null)
                                                                     then category_id = :cid
                                                                 else true
                                                          end);""", source ,new TotalFilesMapper()).getFirst();
    }
}
