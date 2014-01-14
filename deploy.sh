mvn install --settings target/CM/settings.xml
if [ $? -ne 0 ]; then
  exit -1
fi

#
# Ignore pull requests
if [ $TRAVIS_SECURE_ENV_VARS == 'true' ]; then
echo "Attempting deployment..."

if [ $TRAVIS_PULL_REQUEST == 'false' ]; then
  echo "Not a pull request, deploying..."
  mvn deploy --settings target/CM/settings.xml

fi
fi
