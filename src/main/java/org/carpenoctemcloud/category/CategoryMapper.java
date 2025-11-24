package org.carpenoctemcloud.category;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;

/**
 * Maps a result set onto a category entity.
 */
public class CategoryMapper implements RowMapper<Category> {

    /**
     * Default constructor, does not provide functionality.
     */
    public CategoryMapper() {
    }

    @Override
    public Category mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new Category(rs.getInt("id"), rs.getString("name"), rs.getString("svg_path"));
    }
}
