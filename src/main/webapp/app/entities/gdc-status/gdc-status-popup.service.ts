import {Injectable, Component} from "@angular/core";
import {Router} from "@angular/router";
import {NgbModal, NgbModalRef} from "@ng-bootstrap/ng-bootstrap";
import {GdcStatus} from "./gdc-status.model";
import {GdcStatusService} from "./gdc-status.service";
@Injectable()
export class GdcStatusPopupService {
	private isOpen = false;

	constructor(private modalService: NgbModal,
				private router: Router,
				private gdcStatusService: GdcStatusService) {
	}

	open(component: Component, id?: number | any): NgbModalRef {
		if (this.isOpen) {
			return;
		}
		this.isOpen = true;

		if (id) {
			this.gdcStatusService.find(id).subscribe(gdcStatus => {
				this.gdcStatusModalRef(component, gdcStatus);
			});
		} else {
			return this.gdcStatusModalRef(component, new GdcStatus());
		}
	}

	gdcStatusModalRef(component: Component, gdcStatus: GdcStatus): NgbModalRef {
		let modalRef = this.modalService.open(component, {size: 'lg', backdrop: 'static'});
		modalRef.componentInstance.gdcStatus = gdcStatus;
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
