import {Injectable, Component} from "@angular/core";
import {Router} from "@angular/router";
import {NgbModal, NgbModalRef} from "@ng-bootstrap/ng-bootstrap";
import {InactiveReason} from "./inactive-reason.model";
import {InactiveReasonService} from "./inactive-reason.service";
@Injectable()
export class InactiveReasonPopupService {
	private isOpen = false;

	constructor(private modalService: NgbModal,
				private router: Router,
				private inactiveReasonService: InactiveReasonService) {
	}

	open(component: Component, id?: number | any): NgbModalRef {
		if (this.isOpen) {
			return;
		}
		this.isOpen = true;

		if (id) {
			this.inactiveReasonService.find(id).subscribe(inactiveReason => {
				this.inactiveReasonModalRef(component, inactiveReason);
			});
		} else {
			return this.inactiveReasonModalRef(component, new InactiveReason());
		}
	}

	inactiveReasonModalRef(component: Component, inactiveReason: InactiveReason): NgbModalRef {
		let modalRef = this.modalService.open(component, {size: 'lg', backdrop: 'static'});
		modalRef.componentInstance.inactiveReason = inactiveReason;
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
