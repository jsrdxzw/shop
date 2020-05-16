local methodKey = KEYS[1]

local limit = tonumber(ARGV[1])

local count = tonumber(redis.call('get', methodKey) or "0")

if count + 1 > limit then
    return false
else
    redis.call('INCRBY',methodKey,1)
    redis.call('EXPIRE',methodKey,1)
    return true
end
