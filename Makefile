build:
	./gradlew -t build

webpack:
	cd webpack && npm i && npm run dev

.PHONY: webpack build
