-- all karts
select * from passages p
                  inner join karts k  on k.id = p.kart_id
order by passage_time;

-- laps verification


-- lap results
select
    k.kart_number,
    COUNT(p.id) as total_laps,
    r.started_at as start_time,
    MAX(p.passage_time) as finish_time,
    MAX(p.passage_time) - r.started_at as total_duration
from karts k join
     races r on k.race_id = r.id
             join passages p ON k.id = p.kart_id
group by k.id, k.kart_number, r.started_at
order by total_duration;