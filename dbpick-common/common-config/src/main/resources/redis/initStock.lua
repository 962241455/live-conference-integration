local totalkey = KEYS[1]
local current_key = KEYS[2]
local totalnum = tonumber(ARGV[1])

redis.call('set', totalkey, totalnum)
redis.call('set', current_key, totalnum)

return totalnum