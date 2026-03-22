#!/bin/bash
# Configuration
API_URL="http://localhost:8080"
KARTS=(33 44 16 55 11)
TOTAL_LAPS=5
RACE_NAME="Grand Prix $(date +%Y-%m-%d)"

# 1. Start the Race
echo "Creating race..."
RESPONSE=$(curl -s -X POST "$API_URL/races" \
  -H "Content-Type: application/json" \
  -d "{
    \"name\": \"$RACE_NAME\",
    \"totalLaps\": $TOTAL_LAPS,
    \"kartNumbers\": [$(IFS=,; echo "${KARTS[*]}")]
  }")

# Extract Race ID
RACE_ID=$(echo "$RESPONSE" | grep -oE '[0-9a-f]{8}-([0-9a-f]{4}-){3}[0-9a-f]{12}')
if [ -z "$RACE_ID" ]; then
  echo "Failed to start race. Response: $RESPONSE"
  exit 1
fi
echo "Race Started. ID: $RACE_ID"

# 2. Simulate Laps
for ((lap=1; lap<=TOTAL_LAPS; lap++)); do
  echo "Lap $lap/$TOTAL_LAPS in progress..."

   # Shuffle karts (pure bash way)
   # Objs this wasn't me, ai helped
    SHUFFLED_KARTS=($(for k in "${KARTS[@]}"; do echo "$RANDOM $k"; done | sort -n | cut -d' ' -f2))

  for kart in "${SHUFFLED_KARTS[@]}"; do
    # Generate ISO 8601 local date time
    TIMESTAMP=$(date +"%Y-%m-%dT%H:%M:%S.%3")

    # Record passage
    curl -s -o /dev/null -X POST "$API_URL/passages/record" \
      -H "Content-Type: application/json" \
      -d "{
        \"kartNumber\": $kart,
        \"timestamp\": \"$TIMESTAMP\"
      }"

    # Simulate time between karts (0.1s - 0.5s)
    sleep 0.$((RANDOM % 5 + 1))
  done

  # Simulate lap time (1s - 2s)
  sleep $((RANDOM % 2 + 1))
done

# 3. Finish the Race
echo "Closing race..."
curl -s -X PUT "$API_URL/races/$RACE_ID/finish" > /dev/null

sleep $((RANDOM % 2 + 1))

# 4. Get Results
echo "Fetching Final Results..."
curl -s -X GET "$API_URL/races/$RACE_ID/results"