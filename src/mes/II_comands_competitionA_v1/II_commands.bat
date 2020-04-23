

echo 'sending command2.xml'
nc -u -w 1 127.0.0.1 54321 < command2.xml

sleep 1

echo 'DONE!!'
