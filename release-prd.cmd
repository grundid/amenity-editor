call mvn release:prepare release:perform -P build-prd,osm-prd,ae-postgresql,osm-oauth
explorer target\checkout\target