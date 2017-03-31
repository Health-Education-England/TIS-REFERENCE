import {Injectable, Component} from "@angular/core";
import {Router} from "@angular/router";
import {NgbModal, NgbModalRef} from "@ng-bootstrap/ng-bootstrap";
import {CurriculumSubType} from "./curriculum-sub-type.model";
import {CurriculumSubTypeService} from "./curriculum-sub-type.service";
@Injectable()
export class CurriculumSubTypePopupService {
	private isOpen = false;

	constructor(private modalService: NgbModal,
				private router: Router,
				private curriculumSubTypeService: CurriculumSubTypeService) {
	}

	open(component: Component, id?: number | any): NgbModalRef {
		if (this.isOpen) {
			return;
		}
		this.isOpen = true;

		if (id) {
			this.curriculumSubTypeService.find(id).subscribe(curriculumSubType => {
				this.curriculumSubTypeModalRef(component, curriculumSubType);
			});
		} else {
			return this.curriculumSubTypeModalRef(component, new CurriculumSubType());
		}
	}

	curriculumSubTypeModalRef(component: Component, curriculumSubType: CurriculumSubType): NgbModalRef {
		let modalRef = this.modalService.open(component, {size: 'lg', backdrop: 'static'});
		modalRef.componentInstance.curriculumSubType = curriculumSubType;
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
