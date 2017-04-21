import {Injectable, Component} from "@angular/core";
import {Router} from "@angular/router";
import {NgbModal, NgbModalRef} from "@ng-bootstrap/ng-bootstrap";
import {LeavingDestination} from "./leaving-destination.model";
import {LeavingDestinationService} from "./leaving-destination.service";
@Injectable()
export class LeavingDestinationPopupService {
	private isOpen = false;

	constructor(private modalService: NgbModal,
				private router: Router,
				private leavingDestinationService: LeavingDestinationService) {
	}

	open(component: Component, id?: number | any): NgbModalRef {
		if (this.isOpen) {
			return;
		}
		this.isOpen = true;

		if (id) {
			this.leavingDestinationService.find(id).subscribe(leavingDestination => {
				this.leavingDestinationModalRef(component, leavingDestination);
			});
		} else {
			return this.leavingDestinationModalRef(component, new LeavingDestination());
		}
	}

	leavingDestinationModalRef(component: Component, leavingDestination: LeavingDestination): NgbModalRef {
		let modalRef = this.modalService.open(component, {size: 'lg', backdrop: 'static'});
		modalRef.componentInstance.leavingDestination = leavingDestination;
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
