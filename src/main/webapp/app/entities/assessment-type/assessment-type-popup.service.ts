import {Injectable, Component} from "@angular/core";
import {Router} from "@angular/router";
import {NgbModal, NgbModalRef} from "@ng-bootstrap/ng-bootstrap";
import {AssessmentType} from "./assessment-type.model";
import {AssessmentTypeService} from "./assessment-type.service";
@Injectable()
export class AssessmentTypePopupService {
	private isOpen = false;

	constructor(private modalService: NgbModal,
				private router: Router,
				private assessmentTypeService: AssessmentTypeService) {
	}

	open(component: Component, id?: number | any): NgbModalRef {
		if (this.isOpen) {
			return;
		}
		this.isOpen = true;

		if (id) {
			this.assessmentTypeService.find(id).subscribe(assessmentType => {
				this.assessmentTypeModalRef(component, assessmentType);
			});
		} else {
			return this.assessmentTypeModalRef(component, new AssessmentType());
		}
	}

	assessmentTypeModalRef(component: Component, assessmentType: AssessmentType): NgbModalRef {
		let modalRef = this.modalService.open(component, {size: 'lg', backdrop: 'static'});
		modalRef.componentInstance.assessmentType = assessmentType;
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
