# web-crawler

## Overview
Basic web crawler in Java to visit all pages in a specific site and output the findings to a file.

The web crawler will visit a page based on the following:
* Page has not been visited
* Page protocol matches the specified site's protocol
* Page is not external
* Page content type is text/html

## Setup
```
git clone https://github.com/petermatano/web-crawler.git
cd web-crawler
mvn package
```

## Usage
Run: 
```
java -jar target/web-crawler-1.0-SNAPSHOT.jar ${url_to_crawl}
```

Example:
```
java -jar target/web-crawler-1.0-SNAPSHOT.jar https://example.com/
```
