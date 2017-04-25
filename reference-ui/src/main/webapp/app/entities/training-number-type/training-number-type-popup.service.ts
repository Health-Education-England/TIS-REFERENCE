import {Injectable, Component} from "@angular/core";
import {Router} from "@angular/router";
import {NgbModal, NgbModalRef} from "@ng-bootstrap/ng-bootstrap";
import {TrainingNumberType} from "./training-number-type.model";
import {TrainingNumberTypeService} from "./training-number-type.service";
@Injectable()
export class TrainingNumberTypePopupService {
	private isOpen = false;

	constructor(private modalService: NgbModal,
				private router: Router,
				private trainingNumberTypeService: TrainingNumberTypeService) {
	}

	open(component: Component, id?: number | any): NgbModalRef {
		if (this.isOpen) {
			return;
		}
		this.isOpen = true;

		if (id) {
			this.trainingNumberTypeService.find(id).subscribe(trainingNumberType => {
				this.trainingNumberTypeModalRef(component, trainingNumberType);
			});
		} else {
			return this.trainingNumberTypeModalRef(component, new TrainingNumberType());
		}
	}

	trainingNumberTypeModalRef(component: Component, trainingNumberType: TrainingNumberType): NgbModalRef {
		let modalRef = this.modalService.open(component, {size: 'lg', backdrop: 'static'});
		modalRef.componentInstance.trainingNumberType = trainingNumberType;
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
