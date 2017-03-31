import {Injectable, Component} from "@angular/core";
import {Router} from "@angular/router";
import {NgbModal, NgbModalRef} from "@ng-bootstrap/ng-bootstrap";
import {Status} from "./status.model";
import {StatusService} from "./status.service";
@Injectable()
export class StatusPopupService {
	private isOpen = false;

	constructor(private modalService: NgbModal,
				private router: Router,
				private statusService: StatusService) {
	}

	open(component: Component, id?: number | any): NgbModalRef {
		if (this.isOpen) {
			return;
		}
		this.isOpen = true;

		if (id) {
			this.statusService.find(id).subscribe(status => {
				this.statusModalRef(component, status);
			});
		} else {
			return this.statusModalRef(component, new Status());
		}
	}

	statusModalRef(component: Component, status: Status): NgbModalRef {
		let modalRef = this.modalService.open(component, {size: 'lg', backdrop: 'static'});
		modalRef.componentInstance.status = status;
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
