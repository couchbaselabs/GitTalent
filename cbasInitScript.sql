create bucket developerBucket with { "bucket": "default", "nodes": "127.0.0.1" };
create shadow dataset developer on developerBucket;
connect bucket developerBucket with { "password": "" }
