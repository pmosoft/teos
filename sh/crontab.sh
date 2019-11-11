echo "쉘 스크립트 코드"

#!/bin/bash

export JAVA_HOME=/usr/local/jdk1.8.0_181
export PATH=$PATH:/usr/local/jdk1.8.0_181/bin

/usr/bin/spark2-submit
spark2-submit --jars /home/icpap/lib/hiveJdbc11.jar,/home/icpap/lib/ojdbc7.jar --class com.sccomz.scala.schedule.batch.BatchJob /home/icpap/bin/teos.jar 20191111


echo "크론탭 코드"
* 9 * * * /workspace/batchTest.sh >> /workspace/batchTest.sh.log 2>&1