build:
	./gradlew -t build

webpack:
	cd webpack && npm i && npm run dev

testServer:
	$(MAKE) -C testServer run

.PHONY: webpack build testServer
