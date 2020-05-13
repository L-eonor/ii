echo 'sending command5.xml'
nc -u -w 1 127.0.0.1 54321 < command5.xml

sleep 1

echo 'DONE!!'
