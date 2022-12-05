use std::{env, ops::Sub};
extern crate dotenv;
use dotenv::dotenv;
use rocket::{http::ext::IntoCollection, serde::json::serde_json::json};

use crate::models::subject_model::{AddFullExamDto, Subject, Examiner, Duration, Exams};
use mongodb::{
    bson::{doc, extjson::de::Error, oid::ObjectId, DateTime, Bson},
    results::{InsertOneResult, UpdateResult, DeleteResult},
    sync::{Client, Collection},
};

pub struct MongoRepo {
    collection: Collection<Subject>,
}

impl MongoRepo {
    pub fn init() -> Self {
        dotenv().ok();
        let uri = match env::var("MONGOURI") {
            Ok(v) => v.to_string(),
            Err(_) => format!("Error loading mongouri env variable"),
        };

        let client = Client::with_uri_str(uri).unwrap();
        let db = client.database("Cluster0");
        let collection: Collection<Subject> = db.collection("Subject");
        MongoRepo { collection }
    }

    pub fn get_all_subjects(&self) -> Result<Vec<Subject>, Error> {
        let cursor = self
            .collection
            .find(None, None)
            .ok()
            .expect("Error retrieving all subjects");

        let subjects: Vec<Subject> = cursor.map(|doc| doc.unwrap()).collect();

        Ok(subjects)
    }

    pub fn create_subject(&self, new_subject: Subject) -> Result<InsertOneResult, Error> {
        let time_now = DateTime::now();
        let new_document = Subject {
            id: None,
            version: time_now,
            name: new_subject.name,

            exams: new_subject.exams,
        };
        let subject = self
            .collection
            .insert_one(new_document, None)
            .ok()
            .expect("Error creating subject");
        Ok(subject)
    }

    pub fn get_subject_by_id(&self, id: &String) -> Result<Subject, Error> {
        let obj_id = ObjectId::parse_str(id).unwrap();
        let filter = doc! {"_id": obj_id};

        let subject_detail = self
            .collection
            .find_one(filter, None)
            .ok()
            .expect("Error getting subject's detail");
        Ok(subject_detail.unwrap())
    }

   /*  pub fn add_full_exam(&self, examDto: &AddFullExamDto) -> Result<UpdateResult, Error> {

        let subjectResult = self.get_subject_by_id(&examDto.id);

        if(subjectResult.is_err()) {
            Err(subjectResult.err());
        };

        let subject = subjectResult.unwrap();
        let time = DateTime::now();
        let exam = Exams {
            examiner: Examiner {
                name: examDto.examiner_name.to_owned(),
                does_examination: examDto.does_examination.to_owned(),
                timestamp: time
            },
            duration: vec![Duration {
                hours: examDto.hours.to_owned(),
                minutes: examDto.minutes.to_owned(),
                timestamp: time
            }],
            date: vec![time],

            exam_type: examDto.exam_type.to_owned(),

        };

        subject.exams.push(exam);

        let d = subject.exams.into_iter().map(Bson::from).collect::<Vec<_>>();
        let new_doc = doc! {
            "$set": 
            {
                "_id": subject.id,
                "name": subject.name,
                "exams": d
            },
        };

    }
    */

    pub fn delete_subject(&self, id: &String) -> Result<DeleteResult, Error> {
        let obj_id = ObjectId::parse_str(id).unwrap();  
        let filter = doc! {"_id": obj_id};

        let subject_detail= self
            .collection
            .delete_one(filter, None)
            .ok()
            .expect("Error while deleting subject");

        Ok(subject_detail)
    }
}
