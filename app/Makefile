.PHONY: build

test:
	@gradlew test jacocoTestCoverageVerification -i

test-report:
	@gradlew jacocoTestReport

build:
	@gradlew clean build

checkstyle:
	@gradlew checkstyleMain checkstyleTest