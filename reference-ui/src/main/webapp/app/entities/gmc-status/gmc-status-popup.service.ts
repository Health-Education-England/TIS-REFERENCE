import {Injectable, Component} from "@angular/core";
import {Router} from "@angular/router";
import {NgbModal, NgbModalRef} from "@ng-bootstrap/ng-bootstrap";
import {GmcStatus} from "./gmc-status.model";
import {GmcStatusService} from "./gmc-status.service";
@Injectable()
export class GmcStatusPopupService {
	private isOpen = false;

	constructor(private modalService: NgbModal,
				private router: Router,
				private gmcStatusService: GmcStatusService) {
	}

	open(component: Component, id?: number | any): NgbModalRef {
		if (this.isOpen) {
			return;
		}
		this.isOpen = true;

		if (id) {
			this.gmcStatusService.find(id).subscribe(gmcStatus => {
				this.gmcStatusModalRef(component, gmcStatus);
			});
		} else {
			return this.gmcStatusModalRef(component, new GmcStatus());
		}
	}

	gmcStatusModalRef(component: Component, gmcStatus: GmcStatus): NgbModalRef {
		let modalRef = this.modalService.open(component, {size: 'lg', backdrop: 'static'});
		modalRef.componentInstance.gmcStatus = gmcStatus;
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
