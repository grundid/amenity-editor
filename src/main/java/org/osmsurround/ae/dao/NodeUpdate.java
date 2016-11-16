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
package org.osmsurround.ae.dao;

import org.osmsurround.ae.entity.Amenity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.object.SqlUpdate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Types;


@Repository
public class NodeUpdate extends SqlUpdate {

	@Autowired
	public NodeUpdate(DataSource dataSource) {
		setDataSource(dataSource);
		setSql("UPDATE nodes SET version=?, lon=?, lat=? WHERE node_id=?");
		declareParameter(new SqlParameter(Types.BIGINT));
		declareParameter(new SqlParameter(Types.BIGINT));
		declareParameter(new SqlParameter(Types.BIGINT));
		declareParameter(new SqlParameter(Types.BIGINT));
	}

	public void updateNode(Amenity node) {
		update(node.getVersion(), GeoConverter.toDb(node.getLon()), GeoConverter.toDb(node.getLat()), node.getNodeId());
	}
}
