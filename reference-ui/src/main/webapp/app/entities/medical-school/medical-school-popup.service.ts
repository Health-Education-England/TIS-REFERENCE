import {Injectable, Component} from "@angular/core";
import {Router} from "@angular/router";
import {NgbModal, NgbModalRef} from "@ng-bootstrap/ng-bootstrap";
import {MedicalSchool} from "./medical-school.model";
import {MedicalSchoolService} from "./medical-school.service";
@Injectable()
export class MedicalSchoolPopupService {
	private isOpen = false;

	constructor(private modalService: NgbModal,
				private router: Router,
				private medicalSchoolService: MedicalSchoolService) {
	}

	open(component: Component, id?: number | any): NgbModalRef {
		if (this.isOpen) {
			return;
		}
		this.isOpen = true;

		if (id) {
			this.medicalSchoolService.find(id).subscribe(medicalSchool => {
				this.medicalSchoolModalRef(component, medicalSchool);
			});
		} else {
			return this.medicalSchoolModalRef(component, new MedicalSchool());
		}
	}

	medicalSchoolModalRef(component: Component, medicalSchool: MedicalSchool): NgbModalRef {
		let modalRef = this.modalService.open(component, {size: 'lg', backdrop: 'static'});
		modalRef.componentInstance.medicalSchool = medicalSchool;
		modalRef.result.then(result => {
			this.router.navigate([{outlets: {popup: null}}], {replaceUrl: true});
			this.isOpen = false;
		}, (reason) => {
			this.router.navigate([{outlets: {popup: null}}], {replaceUrl: true});
			this.isOpen = false;
		});
		return modalRef;
	}
}
