cd front-end
aws s3 sync . s3://front-end-app/ --acl public-read
