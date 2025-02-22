#!/bin/bash
mvn clean package && java -jar target/fileFilterUtil-1.0-SNAPSHOT.jar -o ./test -o /fnskdjfn /home/helttek/cft_shift_test_task/input_files/in2 /home/helttek/cft_shift_test_task/input_files/in3 /home/helttek/cft_shift_test_task/input_files/in5 /home/helttek/cft_shift_test_task/input_files/in4
