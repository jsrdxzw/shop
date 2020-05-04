local methodKey = KEYS[1]

local limit = tonumber(ARGV[1])

local count = tonumber(redis.call('get', methodKey) or "0")

if count + 1 > limit then
    return false
else
    redis.call('INCRBY',key,1)
    redis.call('EXPIRE',key,1)
    return true
end
