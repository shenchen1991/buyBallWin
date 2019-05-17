select sum(buy_result),count(buy_result),league_name_simply from big_samll_data  where buy_result > 0 or buy_result < 0 GROUP BY league_name_simply;

select sum(buy_result),count(buy_result),league_name_simply from big_samll_data  where buy_result > 0 GROUP BY league_name_simply;

select sum(buy_result),count(buy_result),league_name_simply from big_samll_data  where buy_result < 0 GROUP BY league_name_simply;


select allTemp.league_name_simply,wintemp.count/allTemp.count,allTemp.sum,allTemp.count,
allTemp.sum/allTemp.count
from (select sum(buy_result),count(buy_result) as count,league_name_simply
from big_samll_data  where buy_result > 0 GROUP BY league_name_simply) wintemp
left join (select sum(buy_result) as sum,count(buy_result) as count,league_name_simply
from big_samll_data  where buy_result > 0 or buy_result < 0 GROUP BY league_name_simply) allTemp
on wintemp.league_name_simply = allTemp.league_name_simply
where allTemp.sum > 0 and allTemp.sum/allTemp.count > 0.05