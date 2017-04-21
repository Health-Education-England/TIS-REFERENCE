import {Injectable, Component} from "@angular/core";
import {Router} from "@angular/router";
import {NgbModal, NgbModalRef} from "@ng-bootstrap/ng-bootstrap";
import {RecordType} from "./record-type.model";
import {RecordTypeService} from "./record-type.service";
@Injectable()
export class RecordTypePopupService {
	private isOpen = false;

	constructor(private modalService: NgbModal,
				private router: Router,
				private recordTypeService: RecordTypeService) {
	}

	open(component: Component, id?: number | any): NgbModalRef {
		if (this.isOpen) {
			return;
		}
		this.isOpen = true;

		if (id) {
			this.recordTypeService.find(id).subscribe(recordType => {
				this.recordTypeModalRef(component, recordType);
			});
		} else {
			return this.recordTypeModalRef(component, new RecordType());
		}
	}

	recordTypeModalRef(component: Component, recordType: RecordType): NgbModalRef {
		let modalRef = this.modalService.open(component, {size: 'lg', backdrop: 'static'});
		modalRef.componentInstance.recordType = recordType;
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
