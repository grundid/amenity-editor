CREATE TABLE keyvalues (
    k text,
    v text,
    valuecount integer
);

CREATE TABLE node_tags (
    node_id bigint NOT NULL,
    k text NOT NULL,
    v text NOT NULL
);

CREATE TABLE nodes (
    node_id bigint NOT NULL,
    version integer NOT NULL,
    lat bigint NOT NULL,
    lon bigint NOT NULL
);

INSERT INTO keyvalues (k,v,valuecount) VALUES ('key_a', 'value_a', 10);
INSERT INTO keyvalues (k,v,valuecount) VALUES ('key_a', 'value_b', 20);
INSERT INTO keyvalues (k,v,valuecount) VALUES ('key_b', 'value_a', 30);
INSERT INTO keyvalues (k,v,valuecount) VALUES ('key_b', 'value_b', 40);
INSERT INTO keyvalues (k,v,valuecount) VALUES ('key_b', 'other_value', 50);

INSERT INTO nodes (node_id,version,lat,lon) VALUES (1,1,49000000, 9000000);
INSERT INTO node_tags (node_id,k,v) VALUES (1,'amenity', 'restaurant');
INSERT INTO node_tags (node_id,k,v) VALUES (1,'name', 'Restaurant NE');

INSERT INTO nodes (node_id,version,lat,lon) VALUES (2,1,-1000000, -1000000);
INSERT INTO node_tags (node_id,k,v) VALUES (2,'amenity', 'restaurant');
INSERT INTO node_tags (node_id,k,v) VALUES (2,'name', 'Restaurant SW');

