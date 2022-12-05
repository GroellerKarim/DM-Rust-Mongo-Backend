use mongodb::{bson::DateTime, results::InsertOneResult};
use rocket::{http::Status, log::private::logger, serde::json::Json, State};

use crate::{
    models::subject_model::{CreateSubjectDto, Subject},
    repository::mondodb_repo::MongoRepo,
};

#[post("/subject", data = "<new_subject>")]
pub fn create_subject(
    db: &State<MongoRepo>,
    new_subject: Json<CreateSubjectDto>,
) -> Result<Json<InsertOneResult>, Status> {
    let new_data_string = stringify!(new_subject);
    println!("{}", new_data_string);

    let date = DateTime::now();
    let data = Subject {
        id: None,
        version: date.to_owned(),
        name: new_subject.name.to_owned(),

        //        possible_examiners: new_subject.possible_examiners.to_vec(),
        //      duration: new_subject.duration.to_vec(),
        //    date: new_subject.date.to_vec(),
        possible_examiners: vec![],
        duration: vec![],
        date: vec![],

        exam_type: new_subject.exam_type.to_owned(),
    };

    let data_string = stringify!(data);
    println!("{}", data_string);

    let subject_detail = db.create_subject(data);

    match subject_detail {
        Ok(subject) => Ok(Json(subject)),
        Err(_) => Err(Status::InternalServerError),
    }
}
