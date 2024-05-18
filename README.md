# unblockdev

An application similar to stackoverflow

### Functional Requirements

Top questions to be listed in the dashboard and filter can be provided to fit the users need.
- Only authorised users can create their profile.
- Only authorised users can post questions.
- Only authorised users can answer the questions.
- Only authorised users can answer the answers.
- Only authorised users can add images and videos to their answers.
- Only authorised users can add votes.
- Any user can search the questions by tags or text.
- The authorised user needs to be notified every time when someone answers their questions or answers, adds votes or tags.
- The creator of the question should be able to mark the question as resolved.

### Non-Functional Requirements
- High availability
- Low Latency
- High scalable

### Estimations
- Total Users - 20M
- Questions per day - 5K 
- Answers per day - 10K 
- Active Users per day - 5K 
- Votes per day - 50K 
- Size of each question and answer - 10 MB text + images + videos 
- Votes - 20 Bytes 
- Storage per day - 10 MB (5000 * 10000) + 20 Bytes * (50000) = 500 TB
- DB writes / day -> 50K 
- DB read / day -> 100M 

### Tech Stack
- Java 17 
- Spring Boot 3 
- Postgres 
- Elastic Search 
- Redis 
- Nginx 
- Storage Bucket (S3)

### Architecture Diagram
[View on Eraser![](https://app.eraser.io/workspace/QrsPhApnb2qAdJylVi39/preview?elements=CTxmSfVPRDxWOQ4SKEKdGQ&type=embed)](https://app.eraser.io/workspace/QrsPhApnb2qAdJylVi39?elements=CTxmSfVPRDxWOQ4SKEKdGQ)

### Entity Diagram
[View on Eraser![](https://app.eraser.io/workspace/QrsPhApnb2qAdJylVi39/preview?elements=4BF-vaU_Bvhb4rlVvmwOmg&type=embed)](https://app.eraser.io/workspace/QrsPhApnb2qAdJylVi39?elements=4BF-vaU_Bvhb4rlVvmwOmg)

### Sequence Diagram / Flow Diagram
#### Sign Up and Sign In
The users can sign up with
- Email and password where email_id is verified against an otp.
- Their gmail account.
- Their github account.

- [View on Eraser![](https://app.eraser.io/workspace/QrsPhApnb2qAdJylVi39/preview?elements=M6i5NtH5AyjMBLpbgkbAwA&type=embed)](https://app.eraser.io/workspace/QrsPhApnb2qAdJylVi39?elements=M6i5NtH5AyjMBLpbgkbAwA)

### File Upload Service
[View on Eraser![](https://app.eraser.io/workspace/QrsPhApnb2qAdJylVi39/preview?elements=Rb3B75I_DHORk7v-BeirOA&type=embed)](https://app.eraser.io/workspace/QrsPhApnb2qAdJylVi39?elements=Rb3B75I_DHORk7v-BeirOA)

[View on Eraser![](https://app.eraser.io/workspace/QrsPhApnb2qAdJylVi39/preview?elements=dvQiVrN55rWANpuK0jvOvw&type=embed)](https://app.eraser.io/workspace/QrsPhApnb2qAdJylVi39?elements=dvQiVrN55rWANpuK0jvOvw)

Any file upload like profile_image, images or videos to questions and answers requires the request to come from an authorised client.
The image/video sent to the server is processed and transformed into the desired format and persisted in the Storage Bucket like S3/CDN and the id is persisted in the DataBase.

### File Read Process
[View on Eraser![](https://app.eraser.io/workspace/QrsPhApnb2qAdJylVi39/preview?elements=FOkEpRl3H6wfKcQXiS0JOw&type=embed)](https://app.eraser.io/workspace/QrsPhApnb2qAdJylVi39?elements=FOkEpRl3H6wfKcQXiS0JOw)

### Update Profile
[View on Eraser![](https://app.eraser.io/workspace/QrsPhApnb2qAdJylVi39/preview?elements=lJQkQUkeqUsOUngMGAOGHw&type=embed)](https://app.eraser.io/workspace/QrsPhApnb2qAdJylVi39?elements=lJQkQUkeqUsOUngMGAOGHw)
To Create Profile the user should provide the corresponding token in the Authorization Header
The request api/create-profile, updates the user table with the details provided.
Query

The profile image is stored in a temporary location until the request is made. A cron job runs in the background to clean up the cache if the user did not make the update_profile request and the image stays in the cache for a certain amount of time..

### Top questions
[View on Eraser![](https://app.eraser.io/workspace/QrsPhApnb2qAdJylVi39/preview?elements=-u96JP7dSWmq98ttDr3pwA&type=embed)](https://app.eraser.io/workspace/QrsPhApnb2qAdJylVi39?elements=-u96JP7dSWmq98ttDr3pwA)
To fetch the top questions the user should provide the corresponding token in the Authorization Header.
API -> GET: /api/questions?filter=<filter>
Filters can be of the following types
##### Newest
This results in questions that are trending in recent times based on the questions with more number of viewers, more number of upvotes and the questions that are not resolved in a short period of time. The formula used is

(views_count * vote_count * unresolved)

—----------------------------------------------------

(current_time - question_created_time)

#### Query
``` sql
select title, description, created_time from questions inner join questions_score on questions.id = questions_score.entity_id order by newest_score desc limit 100 
```

##### Active 
The list of the questions that are active in discussion in the recent time. The formula used is

(views_count * vote_count * unresolved)

—------------------------------------------------

(current_time - question_updated_time)

#### Query
``` sql
select * from questions inner join questions_score on questions.id = questions_score.entity_id order by active_score desc limit 100 
```

#### Pagination
The response will have the details about pagination as
"pagination": {
   "total_records": 100,
   "current_page": 1,
   "total_pages": 10,
   "next_page": 2,
   "prev_page": null
 }
The request params should be provided with page=<page_no> and per_page=<number of records per page>

### Post questions
The user can ask a question, the content is sent in the request as in the form of any markup language.
The images and videos are temporarily stored in a cache. After a question is inserted the images and videos are moved from cache to storage bucket.

[View on Eraser![](https://app.eraser.io/workspace/QrsPhApnb2qAdJylVi39/preview?elements=p-jnD9Ph1zK4_-8WZVjOKg&type=embed)](https://app.eraser.io/workspace/QrsPhApnb2qAdJylVi39?elements=p-jnD9Ph1zK4_-8WZVjOKg)

#### Query
```sql
Insert into questions (title, description, user_id, images, videos) values (value1, value2, value3, value4, value5)
```
If the tag does not exist
``` sql
Insert into tags (tag, description, created_by) values (value1, value2, value3)
```

``` sql
Insert into tag_entity (tag_id, entity_id, entity_type) values (value1, question_id, question)

```

### Answer questions / answers

Users can answer to question or answer
When the user answers a question any number of times. For every answer an entry is made to the answers table, answers_entity table. The answers_entity table will have the reference to the question or answer.

[View on Eraser![](https://app.eraser.io/workspace/QrsPhApnb2qAdJylVi39/preview?elements=MUiTS8XOADjy0SPX6QsHLg&type=embed)](https://app.eraser.io/workspace/QrsPhApnb2qAdJylVi39?elements=MUiTS8XOADjy0SPX6QsHLg)

#### Query
``` sql
Insert into answer (title, answer, user_id, images, videos) values (value1, value2, value3, value4, value5)

Insert into answer_entity (entity_id, entity_type) values (value1, value2)

```

The user will be notified in the following cases
Someone answers the question created by a user
Someone answers an answer, the notification is sent to the user who created the question and the user who created the answer.

### Add Votes

The user can add or remove votes. The details are inserted into the votes table and votes_entity table.

[View on Eraser![](https://app.eraser.io/workspace/QrsPhApnb2qAdJylVi39/preview?elements=WQk_wKjGKP3llR_rxMYT2g&type=embed)](https://app.eraser.io/workspace/QrsPhApnb2qAdJylVi39?elements=WQk_wKjGKP3llR_rxMYT2g)

##### Query
``` sql
Insert into vote (user_id, vote) values (value1,(+1 or -1) for upvote or downvote)

Insert into question or answer (entity_id, votes) values (ques_id or ans_id, value2)
Update question or answer set vote = :vote + 1 where ques_id = :id or ans_id = :id

```

The user will be notified whenever someone upvotes or downvotes to their question or answer
### Add Views

For every view irrespective of the user type, the views for the question or answer gets updated in the system.

[View on Eraser![](https://app.eraser.io/workspace/QrsPhApnb2qAdJylVi39/preview?elements=pGBPTRHxRp6nEXzwxjrcdg&type=embed)](https://app.eraser.io/workspace/QrsPhApnb2qAdJylVi39?elements=pGBPTRHxRp6nEXzwxjrcdg)

##### Query
``` sql
Insert into views (user_id, vote) values (value1,(+1 or -1))

Insert into question or answer (entity_id, views) values (ques_id or ans_id, value2)
Update question or answer set views = views + :views where ques_id = :id or ans_id = :id


```

### Search questions by tags

[View on Eraser![](https://app.eraser.io/workspace/QrsPhApnb2qAdJylVi39/preview?elements=6Pwkn7RMCkRvQ8387hhmRw&type=embed)](https://app.eraser.io/workspace/QrsPhApnb2qAdJylVi39?elements=6Pwkn7RMCkRvQ8387hhmRw)

#### Query
``` sql
Select title, description, votes, tags from questions inner join tags_entity on questions.id = tags_entity.question_id inner join tags on tags.id = tags_entity.tag_id

```

### Search questions by text

[View on Eraser![](https://app.eraser.io/workspace/QrsPhApnb2qAdJylVi39/preview?elements=OpPa4fdms3QAtsv3Z3Y9rQ&type=embed)](https://app.eraser.io/workspace/QrsPhApnb2qAdJylVi39?elements=OpPa4fdms3QAtsv3Z3Y9rQ)

#### Query
``` sql 
Select title, description, votes, views from questions inner join answer_entity on questions.id = answer_entity.question_ref_id inner join answers on answer_entity.answer_ref_id = answers.id

```


### Mark as Resolved
[View on Eraser![](https://app.eraser.io/workspace/QrsPhApnb2qAdJylVi39/preview?elements=LoaAucOmnLVCJ2fqxQNF6g&type=embed)](https://app.eraser.io/workspace/QrsPhApnb2qAdJylVi39?elements=LoaAucOmnLVCJ2fqxQNF6g)



### Cron jobs

[View on Eraser![](https://app.eraser.io/workspace/QrsPhApnb2qAdJylVi39/preview?elements=p2loZU20nI-vVzN5l_uBTQ&type=embed)](https://app.eraser.io/workspace/QrsPhApnb2qAdJylVi39?elements=p2loZU20nI-vVzN5l_uBTQ)

Cron jobs can be used in the following scenario
- To update the number of views
- To calculate and update the question_score

### Future Scope
- Related questions for every question.
- Feature to save questions.
- Add custom filters.
- Use analytics to provide suggestions based on users interest.
- Implement rate limiting to avoid DDOS attack on unauthorised calls.
- Add badges and points to users based on the number of questions asked or answered.