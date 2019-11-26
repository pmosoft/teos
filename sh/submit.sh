#############################################
# Shell
#############################################
spark2-shell --master local[*] --driver-memory 8g --executor-memory 64g --jars /home/icpap/bin/teos1.jar,/home/icpap/lib/ojdbc7.jar,/home/icpap/lib/postgresql4.jar,/home/icpap/lib/hiveJdbc11.jar
spark2-shell --master yarn-client --jars /home/icpap/bin/teos1.jar,/home/icpap/lib/ojdbc7.jar,/home/icpap/lib/postgresql4.jar,/home/icpap/lib/hiveJdbc11.jar


#############################################
# Schedule
#############################################


#############################################
# Eng
#############################################
spark2-submit --master yarn --class com.sccomz.scala.job.spark.eng.Los2 /home/icpap/bin/teos1.jar 8463233 5113566
spark2-submit --master yarn --class com.sccomz.scala.job.spark.eng.Los3 /home/icpap/bin/teos1.jar 8463233 5113566
spark2-submit --master yarn --class com.sccomz.scala.job.spark.eng.Los2 /home/icpap/bin/teos.jar 8463233


#############################################
# MakeBin
#############################################
spark2-submit --master yarn --jars /home/icpap/lib/ojdbc7.jar, /home/icpap/lib/hiveJdbc11.jar --class com.sccomz.scala.serialize.MakeBinFile /home/icpap/bin/teos1.jar 8463233
spark2-submit --master local[*] --properties-file spark-defaults.conf --driver-memory 8g --jars /home/icpap/lib/hiveJdbc11.jar --class com.sccomz.scala.serialize.MakeBinFile /home/icpap/bin/teos.jar 8463233

#bf
spark2-submit --master yarn --jars /home/icpap/lib/ojdbc7.jar,/home/icpap/lib/hiveJdbc11.jar --class com.sccomz.scala.serialize.MakeBfBinFile /home/icpap/bin/teos1.jar 8463235


#############################################
# BatchJob
#############################################
spark2-submit --jars /home/icpap/lib/hiveJdbc11.jar,/home/icpap/lib/ojdbc7.jar --class com.sccomz.scala.schedule.batch.BatchJob /home/icpap/bin/teos.jar 20191111


#############################################
# 기타
#############################################
cat /etc/spark/conf/log4j.properties.template
su hdfs
hadoop fs -chmod -R 777 /TEOS/warehouse/SCENARIO_NR_RU_AVG_HEIGHT;
hadoop fs -chown -R icpap:hadoop /TEOS/warehouse/SCENARIO_NR_RU_AVG_HEIGHT
hadoop fs -chown -R icpap:hadoop /TEOS/warehouse/SCENARIO
hadoop fs -chmod -R 777 /TEOS/warehouse/
