spark2-shell --master local[*] --driver-memory 8g --executor-memory 48g --jars /home/icpap/bin/teos1.jar,/home/icpap/lib/ojdbc7.jar,/home/icpap/lib/postgresql4.jar,/home/icpap/lib/hiveJdbc11.jar
spark2-shell --master yarn-client --jars /home/icpap/bin/teos1.jar,/home/icpap/lib/ojdbc7.jar,/home/icpap/lib/postgresql4.jar,/home/icpap/lib/hiveJdbc11.jar

spark2-submit --jars /home/icpap/lib/ojdbc7.jar,/home/icpap/lib/postgresql4.jar,/home/icpap/lib/hiveJdbc11.jar --class com.sccomz.scala.schedule.real.ScheduleDaemon /home/icpap/bin/teos.jar   

spark2-shell --master local[*] --driver-memory 8g --executor-memory 48g --jars /home/icpap/bin/teos1.jar,/home/icpap/lib/ojdbc7.jar,/home/icpap/lib/postgresql4.jar,/home/icpap/lib/hiveJdbc11.jar


spark2-submit --master yarn --jars /home/icpap/lib/hiveJdbc11.jar --class com.sccomz.scala.serialize.MakeBinFile /home/icpap/bin/teos1.jar 8463233

spark2-submit --master yarn --class com.sccomz.scala.job.spark.eng.Los2 /home/icpap/bin/teos1.jar 8463233

spark2-submit --master yarn --class com.sccomz.scala.job.spark.eng.Los2 /home/icpap/bin/teos.jar 8463233


# BatchJob 실행
spark2-submit --jars /home/icpap/lib/hiveJdbc11.jar,/home/icpap/lib/ojdbc7.jar --class com.sccomz.scala.schedule.batch.BatchJob /home/icpap/bin/teos.jar 20191111

