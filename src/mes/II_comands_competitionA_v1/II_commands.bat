
echo 'sending command2.xml'
nc -u -w 1 127.0.0.1 54321 < command2.xml

sleep 5

echo 'sending command1.xml'
nc -u -w 1 127.0.0.1 54321 < command1.xml

sleep 5

echo 'sending command3.xml'
nc -u -w 1 127.0.0.1 54321 < command3.xml

sleep 1

echo 'DONE!!'
