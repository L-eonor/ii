

echo 'sending command3.xml'
nc -u -w 1 127.0.0.1 54321 < command3.xml

sleep 1

echo 'DONE!!'
