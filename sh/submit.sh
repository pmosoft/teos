spark2-shell --master local[*] --jars /home/icpap/bin/teos.jar,/home/icpap/lib/ojdbc7.jar,/home/icpap/lib/postgresql4.jar,/home/icpap/lib/hiveJdbc11.jar,/home/icpap/lib/ImpalaJDBC41.jar

spark2-shell --master local[*] --driver-memory 2g --executor-memory 10g --jars /home/icpap/bin/teos.jar,/home/icpap/lib/ojdbc7.jar,/home/icpap/lib/postgresql4.jar,/home/icpap/lib/hiveJdbc11.jar,/home/icpap/lib/ImpalaJDBC41.jar

spark2-shell --deploy-mode client --master local[*] --driver-memory 2g --executor-memory 10g --jars /home/icpap/lib/ojdbc7.jar,/home/icpap/lib/hiveJdbc11.jar, /home/icpap/bin/teos.jar

spark2-shell --deploy-mode client --driver-memory 2g --executor-memory 10g --jars /home/icpap/lib/ojdbc7-12.1.0.2.jar,/home/icpap/bin/teos.jar
spark2-shell --deploy-mode client --driver-memory 2g --executor-memory 10g --jars /home/icpap/lib/ojdbc7-12.1.0.2.jar /home/icpap/bin/teos.jar
spark2-shell --jars /home/icpap/spark/bin/TeosSpark.jar
spark2-shell --jars /home/str/app/bin/str.jar

#spark2-shell --deploy-mode client --driver-memory 2g --executor-memory 10g --jars /home/xtractor/commoc/ojdbc6.jar, /home…./xtractorSpart.jar
spark2-submit --master yarn --deploy-mode cluster --queue xtractor --executor-cores 4 --jars /home/xtractor/commoc/ojdbc6.jar --class com��.PvStat /home��./xtractorSpart.jar 20181028