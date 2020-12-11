local function bigthan15p (str, num)
    if tonumber(str) == -1 then
       return 1
    end
    local len = string.len(str)
    if len > 15 then
        local strNum = tonumber(string.sub(str, 1, 15))
        return strNum > num/math.pow(10,len-15)
    end
    return tonumber(str) > num
end

local storyKey = KEYS[1]
local maxValue = ARGV[1]
local minValue = ARGV[2]

local curRdsVal = redis.call('INCR',storyKey)

if bigthan15p(minValue, curRdsVal) then
    redis.call('SET', storyKey, minValue)
    return redis.call('INCR', storyKey)
end

if not bigthan15p(maxValue, curRdsVal) then
    redis.call('SET', storyKey, minValue)
    return redis.call('INCR', storyKey)
end

return curRdsVal
