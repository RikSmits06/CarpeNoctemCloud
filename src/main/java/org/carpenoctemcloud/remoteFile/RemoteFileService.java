package org.carpenoctemcloud.remoteFile;

import java.sql.Timestamp;
import java.util.List;
import org.carpenoctemcloud.configuration.ConfigurationConstants;
import org.carpenoctemcloud.indexing.IndexedFile;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Service;

@Service
public class RemoteFileService {

    final NamedParameterJdbcTemplate template;

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

    public List<RemoteFile> searchRemoteFiles(String search, int offset) {
        SqlParameterSource source =
                new MapSqlParameterSource().addValue("offset", offset).addValue("search", search)
                        .addValue("limit", ConfigurationConstants.MAX_FETCH_SIZE);
        return template.query("""
                                      SELECT * FROM remote_file
                                               WHERE search_vector @@ websearch_to_tsquery(:search)
                                               LIMIT :limit OFFSET :offset;
                                      """, source, new RemoteFileMapper());
    }
}
