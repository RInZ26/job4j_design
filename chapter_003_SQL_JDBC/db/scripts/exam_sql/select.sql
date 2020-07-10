SELECT m.name, COUNT(mwu.user_id) FROM meetings AS m
JOIN meeting_with_user AS mwu ON m.id = mwu.meeting_id
WHERE mwu.status_id = 1
GROUP BY m.id

SELECT m.name, COUNT(mwu.user_id) FROM meetings AS m
JOIN meeting_with_user AS mwu ON m.id = mwu.meeting_id
JOIN statuses AS s ON mwu.status_id = s.id
WHERE s.name = 'approved'
GROUP BY m.id

SELECT m.name FROM meetings AS m
LEFT JOIN meeting_with_user AS mwu ON m.id = mwu.meeting_id
WHERE mwu.meeting_id IS NULL