echo 'sending command5.xml'
nc -u -w 100 172.29.0.67 54321 < command5.xml

sleep 1

echo 'DONE!!'
