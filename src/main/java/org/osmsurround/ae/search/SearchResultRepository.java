/**
 * This file is part of Amenity Editor for OSM.
 * Copyright (c) 2001 by Adrian Stabiszewski, as@grundid.de
 *
 * Amenity Editor is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Amenity Editor is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with Amenity Editor.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.osmsurround.ae.search;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;

import javax.sql.DataSource;

import org.osmsurround.ae.dao.GeoConverter;
import org.osmsurround.ae.osm.BoundingBox;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.object.MappingSqlQuery;
import org.springframework.stereotype.Repository;


@Repository
public class SearchResultRepository extends MappingSqlQuery<SearchResult> {

	private final String QUERY = "SELECT n.node_id, n.lon, n.lat, nt.k, nt.v "
			+ "FROM nodes AS n JOIN node_tags AS nt ON n.node_id = nt.node_id "
			+ "WHERE n.lon <= ? AND n.lon >= ? AND n.lat <= ? AND n.lat >= ? ORDER BY n.node_id";

	private final int[] types = { Types.INTEGER, Types.INTEGER, Types.INTEGER, Types.INTEGER };

	@Autowired
	public SearchResultRepository(DataSource dataSource) {
		setDataSource(dataSource);
		setSql(QUERY);
		setTypes(types);
	}

	@Override
	protected SearchResult mapRow(ResultSet rs, int rowNum) throws SQLException {
		return new SearchResult(rs.getLong(1), GeoConverter.fromDb(rs.getLong(2)), GeoConverter.fromDb(rs.getLong(3)),
				rs.getString(4), rs.getString(5));
	}

	public List<SearchResult> search(BoundingBox boundingBox) {
		return execute(new Object[] { GeoConverter.toDb(boundingBox.getEast()),
				GeoConverter.toDb(boundingBox.getWest()), GeoConverter.toDb(boundingBox.getNorth()),
				GeoConverter.toDb(boundingBox.getSouth()) });

	}
}
