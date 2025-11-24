package org.carpenoctemcloud.category;

/**
 * An entity describing the useful parts of the category table in the database.
 *
 * @param id      The id of the category.
 * @param name    The name describing the category.
 * @param svgPath The string representing the path to the svg of the category.
 */
public record Category(int id, String name, String svgPath) {
}
