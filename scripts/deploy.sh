
echo "Starting script...."
git clone --depth=50 --branch=master https://github.com/pjreiniger/maven_repo.git ../../pjreiniger/maven_repo
echo "Cloned repo"
./gradlew build publish -Pmaven_repo=../../pjreiniger/maven_repo
echo "Running with publish"
cd ../../pjreiniger/maven_repo
echo "CD"
ls
./commit_updates.sh

cd $TRAVIS_BUILD