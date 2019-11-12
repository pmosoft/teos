spark2-shell --master local[*] --driver-memory 8g --executor-memory 48g --jars /home/icpap/bin/teos.jar,/home/icpap/lib/ojdbc7.jar,/home/icpap/lib/postgresql4.jar,/home/icpap/lib/hiveJdbc11.jar
spark2-shell --master yarn-client --jars /home/icpap/bin/teos.jar,/home/icpap/lib/ojdbc7.jar,/home/icpap/lib/postgresql4.jar,/home/icpap/lib/hiveJdbc11.jar

spark2-submit --jars /home/icpap/lib/ojdbc7.jar,/home/icpap/lib/postgresql4.jar,/home/icpap/lib/hiveJdbc11.jar --class com.sccomz.scala.schedule.real.ScheduleDaemon /home/icpap/bin/teos.jar   

# BatchJob 실행
spark2-submit --jars /home/icpap/lib/hiveJdbc11.jar,/home/icpap/lib/ojdbc7.jar --class com.sccomz.scala.schedule.batch.BatchJob /home/icpap/bin/teos.jar 20191111