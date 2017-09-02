
git clone --depth=50 --branch=master https://github.com/pjreiniger/maven_repo.git ../../pjreiniger/maven_repo
./gradlew build publish -Pmaven_repo=../../pjreiniger/maven_repo
cd ../../pjreiniger/maven_repo
./commit_updates.sh