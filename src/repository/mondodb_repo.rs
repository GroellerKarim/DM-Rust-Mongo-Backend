use std::env;
extern crate dotenv;
use dotenv::dotenv;
use rocket::serde::json::serde_json::json;

use crate::models::subject_model::Subject;
use mongodb::{
    bson::{doc, extjson::de::Error, oid::ObjectId, DateTime},
    results::InsertOneResult,
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

    pub fn create_subject(&self, new_subject: Subject) -> Result<InsertOneResult, Error> {
        let time_now = DateTime::now();
        let new_document = Subject {
            id: None,
            version: time_now,
            name: new_subject.name,

            possible_examiners: new_subject.possible_examiners,
            duration: new_subject.duration,
            date: new_subject.date,

            exam_type: new_subject.exam_type,
        };
        let subject = self
            .collection
            .insert_one(new_document, None)
            .ok()
            .expect("Error creating subject");
        Ok(subject)
    }

    pub fn get_subject(&self, id: &String) -> Result<Subject, Error> {
        let obj_id = ObjectId::parse_str(id).unwrap();
        let filter = doc! {"_id": obj_id};

        let subject_detail = self
            .collection
            .find_one(filter, None)
            .ok()
            .expect("Error getting subject's detail");
        Ok(subject_detail.unwrap())
    }
}
