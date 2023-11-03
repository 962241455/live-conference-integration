local totalkey = KEYS[1]
local current_key = KEYS[2]
local del = tonumber(ARGV[1])

local total = tonumber(redis.call('get', totalkey))
local current = tonumber(redis.call('get', current_key))
local new_current = current - del
if new_current >0 then
	redis.call('set', current_key, new_current)
	return del
elseif new_current <= 0 then
	redis.call('set', current_key, 0)
	return current
end