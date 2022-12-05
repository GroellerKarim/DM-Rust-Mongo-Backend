use std::{env, ops::Sub};
extern crate dotenv;
use dotenv::dotenv;
use rocket::{http::ext::IntoCollection, serde::json::serde_json::json};

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
}
