****README A4_RS1_A****

* Ambiente de execução: Windows
* Base de Dados: MariadB, com serviço phpMyAdmin fornecido pela FEUP. Os dados para log in estão incluídos no executável
    - Username: up201504515
	- Hostname: db.fe.up.pt
	- Password: nu8maaM6r
* Para correr o executável A4_RS1_A_mes.jar, basta correr o ficheiro "run_java.bat", depois de correr o simulador codesys, cujos parâmetros são:
	- primeiro argumento deve ser o nome do servidor OPC-UA em que se corre, pe "DESKTOP-G3Q32TE"
	- segundo argumento indica se se pretende reiniciar ou descartar as últimas informações, "n" para manter e "y" para descartar.
	 Se se iniciar com "y", será iniciado ignorando todos os dados da base de dados e estado eventual do sfs.
	 Com "n", irá buscar os dados guardados na base de dados bem como tudo o que está a acontecer na fábrica ou aconteceu e nao foi lido pelo MES.
	 - o ficheiro bat reencaminha para ficheiros de texto separados, o std e o stderr, respetivamente em "out.txt" e "error_out.txt"
	 
* O script da estruta da base de dados	 