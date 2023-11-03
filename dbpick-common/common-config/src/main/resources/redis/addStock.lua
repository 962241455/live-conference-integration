local totalkey = KEYS[1]
local current_key = KEYS[2]
local add = tonumber(ARGV[1])

local total = tonumber(redis.call('get', totalkey))
local current = tonumber(redis.call('get', current_key))
local new_current = current + add
if new_current <= total then
	redis.call('set', current_key, new_current)
	return add
elseif new_current > total then
	redis.call('set', current_key, total)
	return total - current
end