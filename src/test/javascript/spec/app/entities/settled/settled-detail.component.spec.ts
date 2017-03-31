import {ComponentFixture, TestBed, async} from "@angular/core/testing";
import {MockBackend} from "@angular/http/testing";
import {Http, BaseRequestOptions} from "@angular/http";
import {OnInit} from "@angular/core";
import {DatePipe} from "@angular/common";
import {ActivatedRoute} from "@angular/router";
import {Observable} from "rxjs/Rx";
import {DateUtils, DataUtils, JhiLanguageService} from "ng-jhipster";
import {MockLanguageService} from "../../../helpers/mock-language.service";
import {MockActivatedRoute} from "../../../helpers/mock-route.service";
import {SettledDetailComponent} from "../../../../../../main/webapp/app/entities/settled/settled-detail.component";
import {SettledService} from "../../../../../../main/webapp/app/entities/settled/settled.service";
import {Settled} from "../../../../../../main/webapp/app/entities/settled/settled.model";

describe('Component Tests', () => {

	describe('Settled Management Detail Component', () => {
		let comp: SettledDetailComponent;
		let fixture: ComponentFixture<SettledDetailComponent>;
		let service: SettledService;

		beforeEach(async(() => {
			TestBed.configureTestingModule({
				declarations: [SettledDetailComponent],
				providers: [
					MockBackend,
					BaseRequestOptions,
					DateUtils,
					DataUtils,
					DatePipe,
					{
						provide: ActivatedRoute,
						useValue: new MockActivatedRoute({id: 123})
					},
					{
						provide: Http,
						useFactory: (backendInstance: MockBackend, defaultOptions: BaseRequestOptions) => {
							return new Http(backendInstance, defaultOptions);
						},
						deps: [MockBackend, BaseRequestOptions]
					},
					{
						provide: JhiLanguageService,
						useClass: MockLanguageService
					},
					SettledService
				]
			}).overrideComponent(SettledDetailComponent, {
				set: {
					template: ''
				}
			}).compileComponents();
		}));

		beforeEach(() => {
			fixture = TestBed.createComponent(SettledDetailComponent);
			comp = fixture.componentInstance;
			service = fixture.debugElement.injector.get(SettledService);
		});


		describe('OnInit', () => {
			it('Should call load all on init', () => {
				// GIVEN

				spyOn(service, 'find').and.returnValue(Observable.of(new Settled(10)));

				// WHEN
				comp.ngOnInit();

				// THEN
				expect(service.find).toHaveBeenCalledWith(123);
				expect(comp.settled).toEqual(jasmine.objectContaining({id: 10}));
			});
		});
	});

});
