spark2-shell --master local[*] --driver-memory 8g --executor-memory 48g --jars /home/icpap/bin/teos1.jar,/home/icpap/lib/ojdbc7.jar,/home/icpap/lib/postgresql4.jar,/home/icpap/lib/hiveJdbc11.jar
spark2-shell --master yarn-client --jars /home/icpap/bin/teos1.jar,/home/icpap/lib/ojdbc7.jar,/home/icpap/lib/postgresql4.jar,/home/icpap/lib/hiveJdbc11.jar

-- schedule 
spark2-submit -Ddm.logging.level=DEBUG" --jars /home/icpap/lib/ojdbc7.jar,/home/icpap/lib/postgresql4.jar,/home/icpap/lib/hiveJdbc11.jar --class com.sccomz.scala.schedule.real.ScheduleDaemon /home/icpap/bin/teos1.jar   

spark-submit
  --master spark://127.0.0.1:7077
  --driver-java-options "-Dlog4j.configuration=file:/path/to/log4j-driver.properties -Ddm.logging.level=DEBUG"
  --conf "spark.executor.extraJavaOptions=-Dlog4j.configuration=file:/path/to/log4j-executor.properties -Ddm.logging.name=myapp -Ddm.logging.level=DEBUG"

spark.sparkContext.setLogLevel("ERROR")

spark2-shell --master local[*] --driver-memory 8g --executor-memory 48g --jars /home/icpap/bin/teos1.jar,/home/icpap/lib/ojdbc7.jar,/home/icpap/lib/postgresql4.jar,/home/icpap/lib/hiveJdbc11.jar

spark2-submit --master yarn --jars /home/icpap/lib/hiveJdbc11.jar --class com.sccomz.scala.serialize.MakeBinFile /home/icpap/bin/teos1.jar 8463233

spark2-submit --master yarn --class com.sccomz.scala.job.spark.eng.Los2 /home/icpap/bin/teos1.jar 8463233 5113566

spark2-submit --master yarn --class com.sccomz.scala.job.spark.eng.Los3 /home/icpap/bin/teos1.jar 8463233 5113566

spark2-submit --master yarn --class com.sccomz.scala.job.spark.eng.Los2 /home/icpap/bin/teos.jar 8463233

# BatchJob 실행
spark2-submit --jars /home/icpap/lib/hiveJdbc11.jar,/home/icpap/lib/ojdbc7.jar --class com.sccomz.scala.schedule.batch.BatchJob /home/icpap/bin/teos.jar 20191111

spark2-submit --master local[*] --properties-file spark-defaults.conf --driver-memory 8g --jars /home/icpap/lib/hiveJdbc11.jar --class com.sccomz.scala.serialize.MakeBinFile /home/icpap/bin/teos.jar 8463233

spark2-shell --master local[*] --driver-memory 8g --executor-memory 48g --jars /home/icpap/bin/teos1.jar,/home/icpap/lib/ojdbc7.jar,/home/icpap/lib/postgresql4.jar,/home/icpap/lib/hiveJdbc11.jar

su hdfs
hadoop fs -chmod -R 777 /TEOS/warehouse/SCENARIO_NR_RU_AVG_HEIGHT;

hadoop fs -chown -R icpap:hadoop /TEOS/warehouse/SCENARIO_NR_RU_AVG_HEIGHT



hadoop fs -chown -R icpap:hadoop /TEOS/warehouse/SCENARIO
hadoop fs -chmod -R 777 /TEOS/warehouse/

