set dirname=%~dp0

java -Dconfig.resource=prod.conf -Dhttp.port=8000 %* -cp "%dirname%/lib/*" play.core.server.NettyServer %dirname%

pause